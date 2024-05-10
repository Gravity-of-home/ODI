interface ILayoutProps {
  children: React.ReactNode;
}

export const Layout = ({ children }: ILayoutProps) => {
  return <div className='w-full h-dvh'>{children}</div>;
};
