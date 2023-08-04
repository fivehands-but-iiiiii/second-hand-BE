import { KeyboardEvent } from 'react';

interface useEnterKeyPressProps {
  onEnterPress: () => void;
}

const useEnterKeyPress = ({ onEnterPress }: useEnterKeyPressProps) => {
  const handleKeyDown = (event: KeyboardEvent) => {
    if (event.key === 'Enter') {
      onEnterPress();
    }
  };

  return { handleKeyDown };
};

export default useEnterKeyPress;
