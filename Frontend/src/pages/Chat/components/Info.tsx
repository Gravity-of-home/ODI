import React, { useState, useEffect } from 'react';
import axios from 'axios';

// TODO ?
// 1. 방 제목 어떻게 넘겨주지
// 2.d
const Info = () => {
  let [title, setTitle] = useState('');

  return (
    <div>
      <div className='party-info flex'>
        <p>파티상태</p>
        <p> 출 도착지</p>
        <p>출발시간</p>
      </div>
      <div className='button'>
        <p>1/N 정산하기</p>
      </div>
    </div>
  );
};

export default Info;
