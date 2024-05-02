interface ILayoutProps {
  children: React.ReactNode;
}

export const Layout = ({ children }: ILayoutProps) => {
  return <div className='w-full sm:w-[640px] h-dvh'>{children}</div>;
};
