// BottomSheet.tsx
import React from 'react';
import { motion } from 'framer-motion';
import useBottomSheet from '@/hooks/useBottomSheet.ts';
import BottomSheetContent from '@/components/BottomSheet/BottomSheetContent.tsx';
import BottomSheetHandle from '@/components/BottomSheet/BottomSheetHandle.tsx';
// import ContentHeader from '../SingleContent/SingleContentHeader';
import SvgList from '@/assets/svg/SvgList';

const BottomSheet: React.FC = () => {
  const { sheet, handleUp, content } = useBottomSheet();

  return (
    <>
      <motion.div
        className='fixed top-[90%] flex flex-col z-50 w-[100%] h-[100%] transition-transform duration-400'
        ref={sheet}>
        <div
          className='flex self-center items-center justify-center w-[30%] h-[4%] bg-black rounded-full z-50 gap-2 text-white cursor-pointer'
          // NOTE : 페이지 이동 해서 이용하는거 하나 만들기
          onClick={handleUp}>
          <SvgList className='w-[20%] h-[80%]' />
          <p className='flex justify-center items-center'>목록 보기</p>
        </div>
        <div className='mt-4 bg-white rounded-t-3xl w-[100%] h-[90%]'>
          <BottomSheetHandle />
          {/* <div className='w-[100%] h-[7%] bg-blue-300'>여기 필터 들어가야지 ?</div> */}
          <div ref={content} className='w-[100%] h-[100%] bg-black overflow-scroll'>
            <div className='w-[100%] h-[100%]'>
              <BottomSheetContent />
            </div>
          </div>
        </div>
      </motion.div>
    </>
  );
};

export default BottomSheet;
