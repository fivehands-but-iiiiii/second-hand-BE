import { ChangeEvent, useCallback, useState } from 'react';

const useInput = (initialValue: string) => {
  const [value, setValue] = useState(initialValue);

  const onChange = useCallback(
    ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
      setValue(target.value);
    },
    [],
  );

  const onSubmit = useCallback(() => {
    setValue('');
  }, []);

  return { value, onChange, onSubmit };
};

export default useInput;
