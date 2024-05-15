import React, { useState, ReactNode } from 'react';

interface TabProps {
  label: string;
  children: ReactNode;
}

interface TabsProps {
  children: React.ReactElement<TabProps>[] | React.ReactElement<TabProps>;
}

export const Tab: React.FC<TabProps> = ({ children }) => <>{children}</>;

export const Tabs: React.FC<TabsProps> = ({ children }) => {
  const [activeTab, setActiveTab] = useState(
    (children as React.ReactElement<TabProps>[])[0].props.label,
  );

  const handleClick = (label: string) => {
    setActiveTab(label);
  };

  return (
    <div>
      <ul className='w-[100%] flex justify-center'>
        {React.Children.map(children, child => (
          <li
            key={child.props.label}
            className={`w-[30%] m-3 p-2 text-center cursor-pointer font-semibold ${child.props.label === activeTab ? 'text-white border-white border-b-2 animate-fadeIn' : 'text-slate-500 hover:text-slate-400'}`}
            onClick={() => handleClick(child.props.label)}>
            {child.props.label}
          </li>
        ))}
      </ul>
      <div className='p-4'>
        {React.Children.map(children, child => {
          if (child.props.label === activeTab) return <div key={child.props.label}>{child}</div>;
          else return null;
        })}
      </div>
    </div>
  );
};
