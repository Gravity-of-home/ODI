import React, { useState, useEffect } from 'react';
import jwtAxios from '@/utils/JWTUtil';
import { motion } from 'framer-motion';
import mapStore from '@/stores/useMapStore';
import useBottomSheet from '@/hooks/useBottomSheet.ts';
import BottomSheetContent from '@/components/BottomSheet/BottomSheetContent.tsx';
import BottomSheetHandle from '@/components/BottomSheet/BottomSheetHandle.tsx';
import SvgList from '@/assets/svg/SvgList';
import SelectFilter from '@/pages/Home/components/SelectFilter';
import { IParty } from '@/types/Party';

const BottomSheet: React.FC = () => {
  const { googleMap, latitude, longitude } = mapStore();
  const { sheet, handleUp, content } = useBottomSheet();
  const [isToday, setIsToday] = useState<boolean>(false);
  const [category, setCategory] = useState<string>('');
  const [distance, setDistance] = useState<string>('');
  const [gender, setGender] = useState<string>('');
  const [departuresDate, setDeparturesDate] = useState<Date | string>('');
  const [listData, setListData] = useState<IParty[]>([]);

  useEffect(() => {
    // console.log(latitude, longitude, googleMap);
    if (googleMap === null) return;
    const getListData = async () => {
      try {
        const response = await jwtAxios.get(
          `api/party-boards?page=0&size=10&sort=${distance}&isToday=${isToday}&departuresDate=${departuresDate}&gender=${gender}&category=${category}&longitude=${longitude}&latitude=${latitude}`,
        );
        console.log('FILTER DATA LIST RESPONSE', response);
        const { content } = response.data.data;
        console.log('FILTER DATA LIST', content);
        setListData(content);
      } catch (error) {
        console.error('FILTER DATA LIST ERROR', error);
      }
    };
    getListData();
  }, [googleMap, isToday, category, distance, gender, departuresDate]);

  return (
    <>
      <motion.div
        className='fixed top-[90%] flex flex-col z-50 w-[100%] h-[100%] transition-transform duration-400'
        ref={sheet}>
        <div
          className='flex self-center items-center justify-center w-[30%] h-[4%] bg-black rounded-full z-50 gap-2 text-white cursor-pointer'
          onClick={handleUp}>
          <SvgList className='w-[20%] h-[80%]' />
          <p className='flex justify-center items-center'>목록 보기</p>
        </div>
        <div className='mt-4 bg-white rounded-t-3xl w-[100%] h-[90%]'>
          <BottomSheetHandle />
          <div className='w-[100%] bg-white'>
            <SelectFilter
              setIsToday={setIsToday}
              setCategory={setCategory}
              setDistance={setDistance}
              setGender={setGender}
              setDeparturesDate={setDeparturesDate}
            />
          </div>
          <div
            ref={content}
            className='w-[100%] h-dvh flex justify-center bg-white overflow-scroll'>
            <div className='w-[85%] h-[50%] overflow-scroll'>
              <BottomSheetContent partyList={listData} />
            </div>
          </div>
        </div>
      </motion.div>
    </>
  );
};

export default BottomSheet;
