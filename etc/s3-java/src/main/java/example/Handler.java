package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.ObjectMetadata;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String> {

    private static final float MAX_WIDTH = 300;
    private static final float MAX_HEIGHT = 300;
    private final String JPG_TYPE = "jpg";
    private final String JPG_MIME = "image/jpg";
    private final String JPEG_TYPE = "jpeg";
    private final String JPEG_MIME = "image/jpeg";
    private final String PNG_TYPE = "png";
    private final String PNG_MIME = "image/png";
    private final String GIF_TYPE = "gif";
    private final String GIF_MIME = "image/gif";

  private final String REGEX = ".*\\.([^\\.]*)";
  @Override
  public String handleRequest(S3Event s3event, Context context) {
    try {
      S3EventNotificationRecord record = s3event.getRecords().get(0);

      String srcBucket = record.getS3().getBucket().getName();

      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();

      String dstBucket = srcBucket;
      String dstKey = srcKey.replace("origin/","");

      // Infer the image type.
      Matcher matcher = Pattern.compile(REGEX).matcher(srcKey);
      if (!matcher.matches()) {
          return "";
      }
      String imageType = matcher.group(1).toLowerCase();

      if (!(JPG_TYPE.equals(imageType.toLowerCase())) && !(PNG_TYPE.equals(imageType.toLowerCase()))
              && !(JPEG_TYPE.equals(imageType.toLowerCase())) && !(GIF_TYPE.equals(imageType.toLowerCase()))) {
          System.out.println("Skipping non-image " + srcKey);
          return "";
      }

      // Download the image from S3 into a stream
      S3Client s3Client = S3Client.builder().build();
      InputStream s3Object = getObject(s3Client, srcBucket, srcKey);

      // Read the source image and resize it
      BufferedImage srcImage = ImageIO.read(s3Object);
      BufferedImage newImage = resizeImage(srcImage);

      // Re-encode image to target format
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ImageIO.write(newImage, imageType, outputStream);

      // Upload new image to S3
      putObject(s3Client, outputStream, dstBucket, dstKey, imageType);
      return "Ok";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private InputStream getObject(S3Client s3Client, String bucket, String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .build();
    return s3Client.getObject(getObjectRequest);
  }

  private void putObject(S3Client s3Client, ByteArrayOutputStream outputStream,
    String bucket, String key, String imageType) {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(outputStream.size());

      imageType = imageType.toLowerCase();

      switch (imageType) {
          case JPG_TYPE:
              metadata.setContentType(JPG_MIME);
              break;
          case PNG_TYPE:
              metadata.setContentType(PNG_MIME);
              break;
          case JPEG_TYPE:
              metadata.setContentType(JPEG_MIME);
              break;
          case GIF_TYPE:
              metadata.setContentType(GIF_MIME);
              break;
      }

      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .metadata(metadata.getUserMetadata())
        .contentDisposition("inline")
        .build();

      // Uploading to S3 destination bucket
      try {
        s3Client.putObject(putObjectRequest,
          RequestBody.fromBytes(outputStream.toByteArray()));
      }
      catch(AwsServiceException e)
      {
        System.out.println(e.awsErrorDetails().errorMessage());
        System.exit(1);
      }
  }

  /**
   * Resizes (shrinks) an image into a small, thumbnail-sized image.
   * 
   * The new image is scaled down proportionally based on the source
   * image. The scaling factor is determined based on the value of
   * MAX_DIMENSION. The resulting new image has max(height, width)
   * = MAX_DIMENSION.
   * 
   * @param srcImage BufferedImage to resize.
   * @return New BufferedImage that is scaled down to thumbnail size.
   */
  private BufferedImage resizeImage(BufferedImage srcImage) {
    int srcHeight = srcImage.getHeight();
    int srcWidth = srcImage.getWidth();
    // Infer scaling factor to avoid stretching image unnaturally
    float scalingFactor = Math.min(
            MAX_WIDTH / srcWidth, MAX_HEIGHT / srcHeight);
    int width = (int) (scalingFactor * srcWidth);
    int height = (int) (scalingFactor * srcHeight);

    BufferedImage resizedImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = resizedImage.createGraphics();
    // Fill with white before applying semi-transparent (alpha) images
    graphics.setPaint(Color.white);
    graphics.fillRect(0, 0, width, height);
    // Simple bilinear resize
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.drawImage(srcImage, 0, 0, width, height, null);
    graphics.dispose();
    return resizedImage;
  }
}
