import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface IButtonProps {
  state: string;
  role: string;
  partyId: string | undefined;
}

// 파티 상태와 조회하는 사람마다 다르게 버튼을 보여주어야 함
// 1. 모집마감 (팟장 & 파티원, 파티신청자, 게스트)
// 2. 모집중 ( 팟장, & 파티원, 파티신청자, 게스트)
const Button: React.FC<IButtonProps & { fetchData: () => void }> = ({
  state,
  role,
  partyId,
  fetchData,
}) => {
  const nav = useNavigate();

  // 동승 참여 요청
  function RequestMatching() {
    jwtAxios
      .post(`api/parties/${partyId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 201) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
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
    if (role === 'ORGANIZER' || role === 'PARTICIPANT') {
      buttonComponent = (
        <div>
          <button
            onClick={GoChat}
            className='bg-blue-500 hover:bg-blue-700  w-11/12 text-white font-bold py-2 px-4 rounded'>
            <p>팟 채팅</p>
          </button>
        </div>
      );
    } else if (role === null) {
      buttonComponent = (
        <div>
          <button
            onClick={RequestMatching}
            className='rounded-lg w-11/12 h-12 bg-blue-600 text-white'>
            <p>매칭 신청하기</p>
          </button>
        </div>
      );
    } else if (role === 'REQUESTER') {
      buttonComponent = (
        <div>
          <button
            onClick={CancelMatching}
            className='rounded-lg w-11/12 h-12 bg-red-600 text-white'>
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
            className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-1/2'>
            <p>팟 채팅</p>
          </button>
          <div className='w-1/2'>
            <p>모집마감</p>
          </div>
        </div>
      );
    } else if (role === null) {
      buttonComponent = (
        <div>
          <div>
            <p>모집마감</p>
          </div>
        </div>
      );
    }
  }

  return (
    <div className='container p-2'>
      {buttonComponent}
      {/* <ToastContainer autoClose={1000} /> */}
    </div>
  );
};

export default Button;
