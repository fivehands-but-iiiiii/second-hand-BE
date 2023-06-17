import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface LoginButtonsProps {
  handleIdLogin: () => void;
  handleGitHubLogin: () => void;
}

const LoginButtons = ({
  handleIdLogin,
  handleGitHubLogin,
}: LoginButtonsProps) => {
  return (
    <>
      <Button active fullWidth onClick={handleIdLogin}>
        아이디로 로그인
      </Button>
      <MyGitHubButton active fullWidth onClick={handleGitHubLogin}>
        GitHub으로 로그인
      </MyGitHubButton>
      <Button fullWidth icon>
        회원가입
      </Button>
    </>
  );
};

const MyGitHubButton = styled(Button)`
  background-color: black;
`;

export default LoginButtons;
