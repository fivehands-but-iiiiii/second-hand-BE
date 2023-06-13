import Button from '@common/Button';

import { styled, css } from 'styled-components';

interface MenuItem {
  id: string;
  title: string;
  style?: string;
}

interface PopupSheetProps {
  type: 'slideDown' | 'slideUp';
  menu: MenuItem[];
  onClose?: () => void;
}

interface PopupSheetStyleProps {
  option?: string;
  slidedown?: boolean;
}

const PopupSheet = ({ type, menu, onClose }: PopupSheetProps) => {
  const isSlideDown = type === 'slideDown';

  return (
    <>
      {isSlideDown ? (
        <MyMenuPopdown>
          {menu &&
            menu.map(({ id, title, style }) => (
              <MyPopupOption key={id} option={style} slidedown>
                <Button fullWidth icon onClick={() => console.log(1)}>
                  {title}
                </Button>
              </MyPopupOption>
            ))}
        </MyMenuPopdown>
      ) : (
        <>
          <MyPopupBackground />
          <MyPopupSheet>
            <MyMenuPopUp>
              {menu &&
                menu.map(({ id, title, style }) => (
                  <MyPopupOption key={id} option={style}>
                    <Button fullWidth icon onClick={() => console.log(1)}>
                      {title}
                    </Button>
                  </MyPopupOption>
                ))}
            </MyMenuPopUp>
            <Button fullWidth onClick={onClose}>
              취소
            </Button>
          </MyPopupSheet>
        </>
      )}
    </>
  );
};

const MyMenuPopdown = styled.div`
  position: absolute;
  width: 180px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.borderStrong};
  border-radius: 12px;
  background-color: ${({ theme }) => theme.colors.system.background};
`;

const MyPopupBackground = styled.div`
  position: absolute;
  z-index: 1;
  top: 50%;
  left: 50%;
  width: 393px;
  height: 100%;
  background-color: ${({ theme }) => theme.colors.neutral.overlay};
  opacity: 0.5;
  transform: translate(-50%, -50%);
`;

const MyPopupSheet = styled.div`
  position: absolute;
  z-index: 10;
  width: 100%;
  height: fit-content;
  max-height: 90%;
  padding: 0 8px;
  bottom: 0;
  > button {
    margin: 8px 0;
    background-color: ${({ theme }) => theme.colors.neutral.background};
    color: ${({ theme }) => theme.colors.system.default};
    ${({ theme }) => theme.fonts.title3};
    font-weight: 600;
  }
`;

const MyMenuPopUp = styled.div`
  border-radius: 1rem;
  background-color: ${({ theme }) => theme.colors.system.backgroundWeak};
`;

const MyPopupOption = styled.div<PopupSheetStyleProps>`
  ${({ slidedown }) =>
    slidedown
      ? css`
          height: 45px;
          line-height: 45px;
          > button {
            ${({ theme }) => theme.fonts.subhead};
            align-items: flex-start;
          }
        `
      : css`
          height: 60px;
          line-height: 60px;
          > button {
            ${({ theme }) => theme.fonts.title3};
          }
        `}
  > button {
    ${({ option }) => option};
  }
  &:not(:last-child) {
    border-bottom: 0.5px solid
      ${({ theme }) => theme.colors.neutral.borderStrong};
  }
`;

export default PopupSheet;
