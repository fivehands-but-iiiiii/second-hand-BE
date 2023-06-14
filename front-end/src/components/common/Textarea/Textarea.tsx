import { HTMLProps } from 'react';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';
import palette from '@styles/colors';

import { styled } from 'styled-components';
interface TextareaStyleProps extends TextareaProps {
  hasAdorned?: boolean;
}

interface TextareaProps extends HTMLProps<HTMLTextAreaElement> {
  value: string;
  type?: 'default' | 'adorned' | 'chat';
  singleLine?: boolean;
  adorned?: keyof typeof iconTypes;
}

const Textarea = ({
  value,
  type = 'default',
  singleLine = false,
  adorned,
  ...rest
}: TextareaProps) => {
  const textareaTypes = {
    default: MyDefaultTextarea,
    adorned: MyAdornedTextarea,
    chat: MyChatTextarea,
  };
  const MyTextarea = textareaTypes[type];

  return (
    <MyTextareaContainer>
      <MyTextarea
        value={value}
        singleLine={singleLine}
        hasAdorned={!!adorned}
        rows={1}
        {...rest}
      />
      {adorned && (
        <MyAdorned>
          <Icon name={adorned} fill={palette.neutral.textWeak} />
        </MyAdorned>
      )}
    </MyTextareaContainer>
  );
};

const MyTextareaContainer = styled.div`
  position: relative;
`;

const MyTextarea = styled.textarea<TextareaStyleProps>`
  width: 100%;
  line-height: 1.5;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.subhead};
  border: none;
  outline: none;
  resize: none;
  ${({ singleLine }) => singleLine && 'white-space: nowrap;'}
  ::placeholder {
    color: ${({ theme }) => theme.colors.neutral.textWeak};
  }
`;

const MyDefaultTextarea = styled(MyTextarea)`
  padding: 15px;
`;

const MyAdornedTextarea = styled(MyTextarea)`
  height: 36px;
  padding: 7px 32px;
  background-color: ${({ theme }) => theme.colors.system.backgroundWeak};
  border-radius: 10px;
`;

const MyAdorned = styled.div`
  position: absolute;
  margin: 7px;
  top: 0;
`;

const MyChatTextarea = styled(MyTextarea)`
  height: 36px;
  padding: 7px 13px;
  background-color: ${({ theme }) => theme.colors.neutral.background};
  border-radius: 18px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

export default Textarea;
