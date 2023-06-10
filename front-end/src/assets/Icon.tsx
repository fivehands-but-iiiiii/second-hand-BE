import * as icons from '@assets/svgs/index';
import palette from '@styles/colors';

interface IconProps {
  iconName: keyof typeof icons;
  width?: number;
  height?: number;
  fill?: string;
}

const Icon = ({
  iconName,
  width = 21,
  height = width,
  fill = palette.neutral.text,
}: IconProps) => {
  const IconComponent = icons[iconName];

  return <IconComponent width={width} height={height} fill={fill} />;
};

export default Icon;
