const StateBadge = (state: any) => {
  return (
    <div
      className={`py-3 badge flex justify-center rounded ${state === 'GATHERING' ? 'bg-blue-100 text-blue-500' : 'bg-red-100 text-red-500'} font-bold`}>
      <div className='flex text-center '>
        <p>{state === 'GATHERING' ? '모집중' : '모집마감'}</p>
      </div>
    </div>
  );
};

export default StateBadge;
