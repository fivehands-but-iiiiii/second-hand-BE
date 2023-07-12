import { useCallback, useState, ChangeEvent } from 'react';

const useInput = (initialValue: string) => {
  const [value, setValue] = useState(initialValue);

  const onChange = useCallback(
    ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
      setValue(target.value);
    },
    [],
  );

  const onSubmit = useCallback(() => {
    // TODO: submit 기능 구현
    setValue('');
  }, []);

  return { value, onChange, onSubmit };
};

export default useInput;
