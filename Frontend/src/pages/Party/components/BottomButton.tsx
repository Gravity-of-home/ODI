import React, { useState, useEffect } from 'react';
import axios from 'axios';

// TODO ?

interface IButtonProps {
  state: string;
  role: string;
  partyId: number;
}

const Button: React.FC<IButtonProps> = ({ state, role, partyId }) => {
  // 모집마감 (팟장 & 파티원, 게스트)
  // 모집중 ( 팟장, & 파티원, 게스트)

  const memberId = 3;
  function RequestMatching() {
    axios
      .post(`http://localhost:8080/api/parties/${partyId}/${memberId}`, {
        headers: {
          AUTHORIZATION:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpZCI6IjIiLCJpYXQiOjE3MTQ1NDE2NTMsImV4cCI6MTcxNDU0NDY1M30.hkEC5kS3epI-KE2pqhR_tUzunM3Cz2dBUzsGbD7QAhA',
        },
      })
      .then(res => {
        console.log(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }
  function CancelMatching() {
    axios
      .post(``)
      .then(res => {
        console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  }
  function GoChat() {
    // 채팅방으로 routing
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
            className='rounded-lg w-11/12 h-12 bg-blue-600 text-white'>
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

  return <div className='container p-2'>{buttonComponent}</div>;
};

export default Button;
