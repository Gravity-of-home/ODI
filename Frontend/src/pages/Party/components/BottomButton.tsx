import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface IButtonProps {
  state: string;
  role: string;
  partyId: string | undefined;
  hostGender: string;
  genderRestriction: string;
}

// 파티 상태와 조회하는 사람마다 다르게 버튼을 보여주어야 함
// 1. 모집마감 (팟장 & 파티원, 파티신청자, 게스트)
// 2. 모집중 ( 팟장, & 파티원, 파티신청자, 게스트)
const Button: React.FC<IButtonProps & { fetchData: () => void }> = ({
  state,
  role,
  partyId,
  hostGender,
  genderRestriction,
  fetchData,
}) => {
  const nav = useNavigate();
  const [gender, setGender] = useState<string | undefined>();

  function RequestDisplay() {
    return (
      <div className=' bg-white'>
        <div className=' '>
          <p>매칭 신청 완료</p>
          <p>팟장에게 매칭 신청 알림을 보냈어요</p>
          <p>수락 후에 채팅으로 자세한 장소, 시간을 정해보세요!</p>
        </div>
      </div>
    );
  }

  useEffect(() => {
    const userDataJSON = localStorage.getItem('User');

    if (userDataJSON) {
      const userData = JSON.parse(userDataJSON);
      const userGender = userData?.state?.gender;
      setGender(userGender);
    }
  }, []);

  // 동승 참여 요청
  function RequestMatching() {
    jwtAxios
      .post(`api/parties/${partyId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 201) {
          toast(<RequestDisplay />, { autoClose: false });
        }
        fetchData();
      })
      .catch(err => {
        console.log(err);
      });
  }
  // 동승 참여 요청 취소
  function CancelMatching() {
    jwtAxios
      .delete(`api/parties/${partyId}`)
      .then(res => {
        console.log(res);
        if (res.data.status === 204) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
        }
        fetchData();
      })
      .catch(err => {
        console.log(err);
        toast.error(`${err.data.message}`, { position: 'top-center' });
      });
  }
  // 채팅방으로 routing
  function GoChat() {
    nav(`/party/chat/${partyId}`);
  }

  let buttonComponent;

  if (state === 'GATHERING') {
    if (role === 'ORGANIZER') {
      buttonComponent = (
        <div>
          <button
            onClick={GoChat}
            className='btn btn-block bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4'>
            <p>팟 채팅</p>
          </button>
        </div>
      );
    } else if (role === 'PARTICIPANT') {
      buttonComponent = (
        <div className='flex justify-between gap-x-4'>
          <button
            onClick={GoChat}
            className='w-1/2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'>
            <p>팟 채팅</p>
          </button>
          <button
            onClick={CancelMatching}
            className='w-1/2 bg-gray-200 hover:bg-gray-700 text-purple font-bold py-2 px-4 rounded'>
            <p>신청 취소하기</p>
          </button>
        </div>
      );
    } else if (role === null) {
      if (genderRestriction === 'ANY' || (genderRestriction !== 'ANY' && gender === hostGender)) {
        buttonComponent = (
          <div>
            <button onClick={RequestMatching} className='btn btn-block h-12 bg-blue-600 text-white'>
              <p>매칭 신청하기</p>
            </button>
          </div>
        );
      } else {
        buttonComponent = (
          <div className=''>
            <button className='btn btn-ghost btn-block no-animation btn-disabled'>
              같은 성별끼리만 매칭이 가능해요
            </button>
          </div>
        );
      }
    } else if (role === 'REQUESTER') {
      buttonComponent = (
        <div>
          <button onClick={CancelMatching} className='btn btn-block h-12 bg-red-600 text-white'>
            <p>신청 취소하기</p>
          </button>
        </div>
      );
    }
  } else if (state === 'COMPLETED') {
    if (role === 'ORGANIZER' || role === 'PARTICIPANT') {
      buttonComponent = (
        <div className='flex justify-between'>
          <button
            onClick={GoChat}
            className='btn  bg-blue-500 hover:bg-blue-700 text-white font-bold w-7/12'>
            <p>팟 채팅</p>
          </button>

          <button className='btn btn-ghost btn-block no-animation btn-disabled w-5/12'>
            모집마감
          </button>
        </div>
      );
    } else if (role === null) {
      buttonComponent = (
        <div className=''>
          <button className='btn btn-ghost btn-block no-animation btn-disabled'>모집마감</button>
        </div>
      );
    }
  }

  return <div className=' fixed bottom-0 w-full z-10 bg-white p-3'>{buttonComponent}</div>;
};

export default Button;
