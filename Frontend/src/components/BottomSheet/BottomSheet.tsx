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
    const getListData = async () => {
      if (googleMap === null) return;
      try {
        const response = await jwtAxios.get(
          `api/party-boards?page=0&size=10&sort=${distance}&isToday=${isToday}&departuresDate=${departuresDate}&gender=${gender}&category=${category}&longitude=${longitude}&latitude=${latitude}`,
          // `/api/party-boards?page=0&size=10&sort=${distance}&isToday=false&departuresDate=&gender=&category=&longitude=${longitude}&latitude=${latitude}`,
          // `api/party-boards?page=0&size=5&sort=distance,desc&isToday=false&departuresDate=&gender=&category=&longitude=128.41936482396736&latitude=36.10441210902909`,
        );
        const { content } = response.data.data;
        // console.log(response);
        console.log('FILTER DATA LIST', content);
        setListData(content);
      } catch (error) {
        console.error('FILTER DATA LIST ERROR', error);
      }
    };
    getListData();
  }, [isToday, category, distance, gender, departuresDate]);

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
          <div className='w-[100%] bg-black'>
            <SelectFilter
              setIsToday={setIsToday}
              setCategory={setCategory}
              setDistance={setDistance}
              setGender={setGender}
              setDeparturesDate={setDeparturesDate}
            />
          </div>
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
