import { Link } from 'react-router-dom';

import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface LoginButtonsProps {
  OAUTH_URL: string;
  onLoginButton: () => void;
  onCreateAccount: () => void;
}

const LoginButtons = ({
  OAUTH_URL,
  onLoginButton,
  onCreateAccount,
}: LoginButtonsProps) => {
  return (
    <>
      <Button active fullWidth onClick={onLoginButton} type="submit">
        아이디로 로그인
      </Button>
      <MyGitHubButton fullWidth>
        <Link to={OAUTH_URL}>GitHub으로 로그인</Link>
      </MyGitHubButton>
      <Button fullWidth icon onClick={onCreateAccount}>
        회원가입
      </Button>
    </>
  );
};

const MyGitHubButton = styled(Button)`
  background-color: ${({ theme }) => theme.colors.accent.textWeak};
  > a {
    width: 100%;
    color: ${({ theme }) => theme.colors.accent.text};
  }
`;

export default LoginButtons;
