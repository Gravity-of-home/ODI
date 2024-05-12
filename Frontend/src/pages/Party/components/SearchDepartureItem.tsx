import SvgGoInside from '@/assets/svg/SvgGoInside';
import { IPlaceInfo } from '@/types/Party';
import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import partyStore from '@/stores/usePartyStore';

const SearchDepartureItem = (item: IPlaceInfo) => {
  const dataModalRef = useRef<HTMLDialogElement>(null);
  const { setDepartures } = partyStore();
  const nav = useNavigate();

  const openSetDepartureModal = () => {
    if (dataModalRef.current) {
      dataModalRef.current.showModal();
    }
  };

  const setData = () => {
    setDepartures?.(item.placeName as string, {
      latitude: item.geoPoint!.latitude,
      longitude: item.geoPoint!.longitude,
    });
    nav('/party-boards', { replace: true });
  };

  let setDepartureModal = (
    <>
      <dialog ref={dataModalRef} id='my_modal_4' className='modal'>
        <div className='modal-box w-11/12'>
          <h3 className='font-bold text-[20px]'>출발지로 설정하시겠습니까?</h3>
          <div className='mt-1 border border-gray-500'></div>
          <h4 className='mt-3 text-lg'>{item.placeName}</h4>

          <div className='modal-action'>
            <form method='dialog'>
              <button className='btn btn-sm btn-circle btn-ghost absolute right-5 top-5'>✕</button>
              <button className='btn bg-OD_PURPLE text-white' onClick={setData}>
                설정하기
              </button>
            </form>
          </div>
        </div>
      </dialog>
    </>
  );

  return (
    <>
      {'출발지 검색을 통해 출발지 설정하는 모달' && setDepartureModal}
      <div className='w-[100%] h-[10%] flex justify-center border-b border-slate-700 hover:bg-slate-500'>
        <div className='w-[90%]' onClick={openSetDepartureModal}>
          <div className='w-[100%] flex justify-between my-2'>
            <div className='font-semibold'>{item.placeName}</div>
            <div className='w-[10%] flex justify-center items-center'>
              <SvgGoInside />
            </div>
          </div>
          <div>
            {item.roadNameAddress} | {item.distance}
          </div>
        </div>
      </div>
    </>
  );
};

export default SearchDepartureItem;
