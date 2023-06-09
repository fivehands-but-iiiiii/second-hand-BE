interface Theme {
  neutral: {
    text: string;
    textWeak: string;
    textStrong: string;
    background: string;
    backgroundWeak: string;
    backgroundBold: string;
    backgroundBlur: string;
    border: string;
    borderStrong: string;
    overlay: string;
  };
  accent: {
    text: string;
    textWeak: string;
    backgroundPrimary: string;
    backgroundSecondary: string;
  };
  system: {
    default: string;
    warning: string;
    background: string;
    backgroundWeak: string;
  };
}

const theme: Theme = {
  neutral: {
    text: '$gray900',
    textWeak: '$gray800',
    textStrong: '$black',
    background: '$white',
    backgroundWeak: '$gray50',
    backgroundBold: '$gray400',
    backgroundBlur: '$gray100',
    border: '$gray500',
    borderStrong: '$gray700',
    overlay: '$gray600',
  },
  accent: {
    text: '$white',
    textWeak: '$black',
    backgroundPrimary: '$orange',
    backgroundSecondary: '$mint',
  },
  system: {
    default: '$blue',
    warning: '$red',
    background: '$white',
    backgroundWeak: '$gray200',
  },
};
export default theme;
