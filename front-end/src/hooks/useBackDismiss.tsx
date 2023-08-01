import { useEffect, useCallback } from 'react';

interface UseBackDismissProps {
  isOpen: boolean,
  onClose: () => void
}

const useBackDismiss = ({isOpen, onClose}:UseBackDismissProps ) => {
  const handlePopState = useCallback(
    (event: PopStateEvent) => {
      if (isOpen) {
        event.preventDefault();
        onClose();
      }
    },
    [isOpen, onClose]
  );

  useEffect(() => {
    window.addEventListener('popstate', handlePopState);

    return () => {
      window.removeEventListener('popstate', handlePopState);
    };
  }, [handlePopState]);

  useEffect(() => {
    if (isOpen) {
      window.history.pushState(null, document.title, window.location.href);
    }
  }, [isOpen]);
};

export default useBackDismiss;
