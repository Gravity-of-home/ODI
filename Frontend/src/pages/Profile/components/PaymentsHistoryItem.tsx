import React from 'react';
import { IPaymentsHistory } from '@/types/PayHistory';

interface PaymentsHistoryItemProps {
  item: IPaymentsHistory;
}

const PaymentsHistoryItem: React.FC<PaymentsHistoryItemProps> = ({ item }) => {
  const payList = {
    PREPAYMENT: '선결제',
    SETTLEMENT: '정산',
    CHARGE: '충전',
  };
  return (
    <div className='w-[90%] h-[100%] flex border-b-[1px] border-dashed'>
      <div className='w-[30%] h-[100%] flex justify-center items-center'>
        {payList[item.type] === '충전' ? (
          <img
            src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Money%20with%20Wings.png'
            alt='Money with Wings'
            width='50px'
            height='50px'
          />
        ) : (
          <img
            src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Automobile.png'
            alt='Automobile'
            width='50px'
            height='50px'
          />
        )}
      </div>
      <div className='w-[70%] h-[100%] flex flex-col justify-evenly'>
        <div className='h-[25%] flex justify-between'>
          <div
            className={`h-[100%] text-${payList[item.type] === '충전' ? 'blue-500' : 'red-500'} font-bold`}>
            {payList[item.type]}
          </div>
          <div className='h-[100%] text-gray-500'>{item.createdAt?.split(' ')[0]}</div>
        </div>
        <div className='h-[25%] text-black font-bold'>
          {(item.amount as number) > 0 ? '+ ' : '- '}
          {(item.amount as number) < 0 ? (item.amount as number) * -1 : (item.amount as number)}
        </div>
        <div className='h-[25%] text-gray-500'>
          {item.type === 'CHARGE' ? item.content : item.detailContent!.split('(')[0]}
        </div>
        <div className='h-[25%] text-gray-500'>
          {`${item.type === 'CHARGE' ? '' : '(' + item.detailContent!.split('(')[1]}`}
        </div>
        {/* <div className='text-white'>Detail Content: {item.detailContent}</div> */}
        {/* <div className='text-white'>Party ID: {item.partyId}</div> */}
        {/* <div className='text-white'>Payment ID: {item.paymentId}</div> */}
      </div>
    </div>
  );
};

export default PaymentsHistoryItem;
