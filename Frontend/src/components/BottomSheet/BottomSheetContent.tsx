import React from 'react';
// import useStore from '../../../../state/store/ContentStore';

const BottomSheetContent: React.FC = () => {
  // const { totalData } = useStore(state => state);
  const totalData = false;

  if (!totalData) {
    return (
      <div className='flex flex-col items-center justify-center gap-6 mt-3 w-[100%]'>
        {/* <img src="/public/png/Simbol.png" alt="Symbol" /> */}
        <p className='text-2xl font-medium text-gray-400'>데이터가 없습니다 :(</p>
      </div>
    );
    // } else {
    //     return (
    //         <div className="modal-content content-wrapper">
    //             <SingleContent from="BottomSheet" />
    //         </div>
    //     );
  }
};

export default BottomSheetContent;
