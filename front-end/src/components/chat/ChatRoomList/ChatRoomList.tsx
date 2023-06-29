import ChatListItem from './ChatListItem';

interface ChatRoomListProps {
  chatItems: ChatListItem[];
}
const ChatRoomList = ({ chatItems }: ChatRoomListProps) => {
  return (
    <>
      {chatItems.map((chat) => (
        <ChatListItem key={chat.id} chatItem={chat} />
      ))}
    </>
  );
};

export default ChatRoomList;
