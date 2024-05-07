import React, { useState, useEffect } from 'react';
import axios from 'axios';

interface IInfoProps {
  departuresName: string;
  arrivalsName: string;
  departuresDate: string;
  state: string;
}

const Info: React.FC<IInfoProps> = ({ departuresName, arrivalsName, departuresDate, state }) => {
  let stateComponent;
  if (state === '') {
    stateComponent = <div className='badge badge-outline'>{state}</div>;
  } else if (state === '') {
    stateComponent = <div className='badge badge-primary badge-outline'>{state}</div>;
  } else if (state === 'COMPLETED') {
    stateComponent = <div className='badge badge-secondary badge-outline'>모집마감</div>;
  }

  return (
    <div>
      <div className='party-info'>
        <div className='flex'>
          <div className='px-4'>{stateComponent}</div>
          <div>
            <p>
              {departuresName} {'>'} {arrivalsName}
            </p>
            <p>{departuresDate}</p>
          </div>
        </div>
      </div>
      <div className='button'>
        <p>1/N 정산하기</p>
      </div>
    </div>
  );
};

export default Info;
