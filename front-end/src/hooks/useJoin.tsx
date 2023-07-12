import { UserInfo, InputFile } from '@components/login/Join';

import useAPI from './useAPI';

interface UseJoinProps {
  files?: InputFile;
  account: UserInfo;
}

const useJoin = () => {
  const { request } = useAPI();

  const join = async ({ files, account }: UseJoinProps) => {
    try {
      if (files?.file) {
        const formData = new FormData();
        formData.append('profileImage', files?.file, files?.file.name);
        const { data: image } = await request({
          url: '/profile/image',
          method: 'post',
          config: {
            data: formData,
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          },
        });
        const updatedAccount: UserInfo = {
          ...account,
          profileImgUrl: image.uploadUrl,
        };
        await request({
          url: '/join',
          method: 'post',
          config: {
            data: updatedAccount,
          },
        });
      } else {
        await request({
          url: '/join',
          method: 'post',
          config: {
            data: account,
          },
        });
      }
      return {
        success: true,
        message: '회원가입이 완료되었어요',
      };
    } catch (error) {
      return {
        success: false,
        message: '회원가입에 실패했어요',
      };
    }
  };

  return { join };
};

export default useJoin;
