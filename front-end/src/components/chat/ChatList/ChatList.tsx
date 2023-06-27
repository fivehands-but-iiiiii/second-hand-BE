import ChatListItem from './ChatListItem';

interface ChatListProps {
  chatItems: ChatListItem[];
}
const ChatList = ({ chatItems }: ChatListProps) => {
  return (
    <>
      {chatItems.map((chat) => (
        <ChatListItem key={chat.id} chatItem={chat} />
      ))}
    </>
  );
};

export default ChatList;
