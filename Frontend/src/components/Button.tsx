interface IButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  label: string;
  width?: string;
  height?: string;
}

export enum ColorPalette {
  PURPLE = '#A93BFF',
  GREEN = '#27E92A',
  SKYBLUE = '#40E5FF',
  YELLOW = '#FBFF42',
  ORANGE = '#FF9928',
  SCARLET = '#F05C4A',
  JORDYBLUE = '#8FB1E1',
  PINK = '#FF70A3',
}

const Button = ({ label, width, height, ...props }: IButtonProps) => {
  return (
    <button className={`flex justify-center items-center w-${width} h-${height}`}>{label}</button>
  );
};
