import { Link } from 'react-router-dom';

import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface LoginButtonsProps {
  handleIdLogin: () => void;
  OAUTH_URL: string;
}

const LoginButtons = ({ handleIdLogin, OAUTH_URL }: LoginButtonsProps) => {
  return (
    <>
      <Button active fullWidth onClick={handleIdLogin}>
        아이디로 로그인
      </Button>
      <MyGitHubButton fullWidth>
        <Link to={OAUTH_URL}>GitHub으로 로그인</Link>
      </MyGitHubButton>
      <Button fullWidth icon>
        회원가입
      </Button>
    </>
  );
};

const MyGitHubButton = styled(Button)`
  background-color: ${({ theme }) => theme.colors.accent.textWeak};
  > a {
    color: ${({ theme }) => theme.colors.accent.text};
  }
`;

export default LoginButtons;
