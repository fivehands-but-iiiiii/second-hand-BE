declare module '*.svg' {
  import { FC, SVGProps } from 'react';
  const ReactComponent: FC<SVGProps<SVGSVGElement>>;
  const src: string;
  export { ReactComponent };
  export default src;
}
