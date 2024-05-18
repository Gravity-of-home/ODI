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
    PAYER_SETTLEMENT: '결제 금액 정산',
  };

  const payListColor = {
    PREPAYMENT: 'red-500',
    SETTLEMENT: 'red-500',
    CHARGE: 'blue-500',
    PAYER_SETTLEMENT: 'blue-500',
  };

  const payListImgae = {
    PREPAYMENT: (
      <img
        src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Automobile.png'
        alt='Automobile'
        width='50px'
        height='50px'
      />
    ),
    SETTLEMENT: (
      <img
        src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Taxi.png'
        alt='Taxi'
        width='50px'
        height='50px'
      />
    ),
    CHARGE: (
      <img
        src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/High%20Voltage.png'
        alt='High Voltage'
        width='50px'
        height='50px'
      />
    ),
    PAYER_SETTLEMENT: (
      <img
        src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Money%20with%20Wings.png'
        alt='Money with Wings'
        width='50px'
        height='50px'
      />
    ),
  };

  return (
    <div className='w-[90%] h-[100%] flex border-b-[1px] border-dashed py-2'>
      <div className='w-[30%] h-[100%] flex justify-center items-center'>
        {payListImgae[item.type]}
      </div>
      <div className='w-[70%] h-[100%] flex flex-col justify-evenly'>
        <div className='h-[15%] flex justify-between'>
          <div className={`h-[100%] text-${payListColor[item.type]} font-bold`}>
            {payList[item.type]}
          </div>
          <div className='h-[100%] text-gray-500'>{item.createdAt?.split(' ')[0]}</div>
        </div>
        <div className='h-[15%] text-black font-bold'>{item.content}</div>
        <div className='h-[15%] text-black font-bold'>
          {(item.amount as number) > 0 ? '+ ' : '- '}
          {(item.amount as number) < 0 ? (item.amount as number) * -1 : (item.amount as number)}
        </div>
        {item.detailContent?.includes('(') ? (
          <>
            <div className='h-[15%] text-gray-500'>
              {item.detailContent!.split('(')[0].length > 20
                ? item.detailContent!.split('(')[0].slice(0, 20) + '...'
                : item.detailContent!.split('(')[0]}
            </div>
            <div className='h-[15%] text-gray-500'>{'(' + item.detailContent!.split('(')[1]}</div>
          </>
        ) : item.detailContent === '' ? (
          ''
        ) : (
          <>
            <div className='h-[15%] text-gray-500'>
              {item.detailContent!.split('[')[0].length > 20
                ? item.detailContent!.split('[')[0].slice(0, 20) + '...'
                : item.detailContent!.split('[')[0]}
            </div>
            <div className='h-[15%] text-gray-500'>{'[' + item.detailContent!.split('[')[1]}</div>
          </>
        )}
        {/* <div className='text-white'>Detail Content: {item.detailContent}</div> */}
        {/* <div className='text-white'>Party ID: {item.partyId}</div> */}
        {/* <div className='text-white'>Payment ID: {item.paymentId}</div> */}
      </div>
    </div>
  );
};

export default PaymentsHistoryItem;
