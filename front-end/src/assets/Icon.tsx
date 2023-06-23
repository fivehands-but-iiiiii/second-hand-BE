import * as iconTypes from '@assets/svgs/index';
import palette from '@styles/colors';

interface IconProps {
  name: keyof typeof iconTypes;
  size?: keyof typeof iconSizes;
  fill?: string;
  className?: string;
  onClick?: () => void;
}

const iconSizes = {
  xxs: 8,
  xs: 14,
  sm: 17,
  md: 20,
  lg: 24,
  xl: 30,
};

const Icon = ({
  name,
  size = 'md',
  fill = palette.neutral.text,
  className,
  onClick,
}: IconProps) => {
  const IconComponent = iconTypes[name];
  const iconSize = iconSizes[size];

  return (
    <IconComponent
      width={iconSize}
      height={iconSize}
      fill={fill}
      className={className}
      onClick={onClick}
    />
  );
};

export default Icon;
