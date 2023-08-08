import { useState } from 'react';
// TODO: hoook으로 이동할찌 삭제할지 결정
const useAlert = () => {
  const [isOpen, setIsOpen] = useState(false);

  const open = () => {
    setIsOpen(true);
  };

  const close = () => {
    setIsOpen(false);
  };

  return { isOpen, open, close };
};

export default useAlert;