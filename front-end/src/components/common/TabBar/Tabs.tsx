import Icon from '@assets/Icon';

import Button from '../Button/Button';

const Tabs = () => {
  return (
    <div>
      <Button>
        <Icon name={'home'}></Icon>
        <span>홈화면</span>
      </Button>
      <Button>
        <Icon name={'newspaper'}></Icon>
        <span>판매내역</span>
      </Button>
      <Button>
        <Icon name={'heart'}></Icon>
        <span>관심목록</span>
      </Button>
      <Button>
        <Icon name={'message'}></Icon>
        <span>채팅</span>
      </Button>
      <Button>
        <Icon name={'person'}></Icon>
        <span>내 계정</span>
      </Button>
    </div>
  );
};

export default Tabs;
