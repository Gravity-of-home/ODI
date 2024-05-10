import type { SVGProps } from 'react';
const SvgGoBack = (props: SVGProps<SVGSVGElement>) => (
  <svg
    width='10'
    height='18'
    viewBox='0 0 7 12'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}>
    <path
      d='M6 1L1 6L6 11'
      stroke='white'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default SvgGoBack;
