import type { SVGProps } from 'react';
const SvgGoInside = (props: SVGProps<SVGSVGElement>) => (
  <svg
    width='7'
    height='12'
    viewBox='0 0 7 12'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}>
    <path
      d='M1 1L6 6L1 11'
      stroke='#A0A0A0'
      strokeWidth='1.5'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
export default SvgGoInside;
