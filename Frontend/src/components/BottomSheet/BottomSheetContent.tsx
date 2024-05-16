import React, { useEffect } from 'react';
// import useStore from '../../../../state/store/ContentStore';

const BottomSheetContent: React.FC = () => {
  // const { totalData } = useStore(state => state);
  const totalData = false;

  if (!totalData) {
    return (
      <div className='flex flex-col items-center justify-center mt-3 gap-6 w-[100%]'>
        {/* <img src="/public/png/Simbol.png" alt="Symbol" /> */}
        <div className='text-lg text-gray-400'>게시글이 없습니다 :(</div>
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
