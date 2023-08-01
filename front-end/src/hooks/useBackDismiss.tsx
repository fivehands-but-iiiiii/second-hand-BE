import { useEffect, useCallback } from 'react';

const useBackDismiss = (isOpen: boolean, onClose: () => void): void => {
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
