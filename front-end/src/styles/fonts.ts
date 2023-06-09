interface Font {
  weight: string;
  size: string;
}

interface Fonts {
  largeTitle: Font;
  title1: Font;
  title2: Font;
  title3: Font;
  headline: Font;
  body: Font;
  callout: Font;
  subhead: Font;
  footnote: Font;
  caption1: Font;
  caption2: Font;
}

const fonts: Fonts = {
  largeTitle: {
    weight: 'regular',
    size: '34px',
  },
  title1: {
    weight: 'regular',
    size: '28px',
  },
  title2: {
    weight: 'regular',
    size: '22px',
  },
  title3: {
    weight: 'regular',
    size: '20px',
  },
  headline: {
    weight: 'semibold',
    size: '17px',
  },
  body: {
    weight: 'regular',
    size: '17px',
  },
  callout: {
    weight: 'regular',
    size: '16px',
  },
  subhead: {
    weight: 'regular',
    size: '15px',
  },
  footnote: {
    weight: 'regular',
    size: '13px',
  },
  caption1: {
    weight: 'regular',
    size: '12px',
  },
  caption2: {
    weight: 'regular',
    size: '11px',
  },
};

export default fonts;
