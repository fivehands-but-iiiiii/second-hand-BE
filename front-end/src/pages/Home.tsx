import { ChangeEvent, useState } from 'react';

import Icon from '@assets/Icon';
import Input from '@components/common/Textarea';
import palette from '@styles/colors';

const INPUT_DUMMY = {
  placeholder: '입력해주세요',
};

const Home = () => {
  const [input, setInput] = useState('');
  return (
    <>
      <Input
        value={input}
        type={'adorned'}
        placeholder={INPUT_DUMMY.placeholder}
        onChange={(e: ChangeEvent<HTMLTextAreaElement>) => {
          console.log('change!');
          setInput(e.target.value);
        }}
      >
        <Icon name={'search'} size="lg" fill={palette.neutral.textWeak} />
      </Input>
    </>
  );
};

export default Home;
