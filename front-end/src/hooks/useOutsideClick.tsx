import { useEffect, useRef } from 'react';

interface UseOutsideClickProps {
  handleOpen: (status: boolean) => void
}

const useOutsideClick = ({ handleOpen }: UseOutsideClickProps) => {
  // 꼭 HTMLDivElement로 해야되는 걸까? 확장성에 대해 생각해보기.
  const ref = useRef<HTMLDivElement | null>(null);

  const handleClickOutside = ({ target }: MouseEvent) => {
    if (ref.current && !ref.current.contains(target as Node)) {
      handleOpen(false);
    }
  }

  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    }
  });

  return ref;
};

export default useOutsideClick;
