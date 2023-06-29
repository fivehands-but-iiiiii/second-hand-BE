import Icon from '@assets/Icon';
import NavBar from '@common/NavBar/NavBar';

const ChatRoom = () => {
  return (
    <>
      <NavBar
        left={
          <button>
            <Icon name={'chevronLeft'} />
            뒤로
          </button>
        }
        center=""
      />
    </>
  );
};

export default ChatRoom;
