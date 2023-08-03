import { useRef, ComponentPropsWithRef, KeyboardEvent, useEffect } from 'react';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';
import palette from '@styles/colors';

import { styled } from 'styled-components';

interface TextareaProps extends ComponentPropsWithRef<'textarea'> {
  value?: string;
  type?: 'default' | 'icon' | 'chat';
  singleLine?: boolean;
  icon?: keyof typeof iconTypes;
  autoFocus?: boolean;
}

const Textarea = ({
  value,
  type = 'default',
  singleLine = false,
  icon,
  autoFocus = false,
  ...rest
}: TextareaProps) => {
  const textareaTypes = {
    default: MyDefaultTextarea,
    icon: MyIconTextarea,
    chat: MyChatTextarea,
  };
  const MyTextarea = textareaTypes[type];
  const textRef = useRef<HTMLTextAreaElement>(null);

  const handleResizeHeight = () => {
    if (!singleLine && textRef.current) {
      textRef.current.style.height = textRef.current.scrollHeight + 'px';
    }
  };

  const handlePrevNewLineOnEnter = (
    event: KeyboardEvent<HTMLTextAreaElement>,
  ) => {
    if (singleLine && event.key === 'Enter') {
      event.preventDefault();
    }
  };

  useEffect(() => {
    if (autoFocus) {
      textRef.current?.focus();
    }
  });

  return (
    <MyTextareaContainer>
      <MyTextarea
        ref={textRef}
        value={value}
        singleLine={singleLine}
        onInput={handleResizeHeight}
        onKeyDown={handlePrevNewLineOnEnter}
        {...rest}
      />
      {icon && (
        <MyIcon>
          <Icon name={icon} fill={palette.neutral.textWeak} />
        </MyIcon>
      )}
    </MyTextareaContainer>
  );
};

const MyTextareaContainer = styled.div`
  width: 100%;
  position: relative;
  display: flex;
`;

const MyTextarea = styled.textarea<TextareaProps>`
  width: 100%;
  line-height: 1.5;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.subhead};
  border: none;
  outline: none;
  resize: none;
  overflow: hidden;
  ${({ singleLine }) => singleLine && 'white-space: nowrap; overflow: hidden;'}
  ::placeholder {
    color: ${({ theme }) => theme.colors.neutral.textWeak};
  }
`;

const MyDefaultTextarea = styled(MyTextarea)`
  padding: 15px;
`;

const MyIconTextarea = styled(MyTextarea)`
  height: 36px;
  padding: 7px 32px;
  background-color: ${({ theme }) => theme.colors.system.backgroundWeak};
  border-radius: 10px;
`;

const MyIcon = styled.div`
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
