import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCookie } from '@/utils/CookieUtil';
import jwtAxios from '@/utils/JWTUtil';

const ChatListPage = () => {
  const [error, setError] = useState(false);
  const [chatList, setChatList] = useState([]);
  const nav = useNavigate();

  const fetchData = async () => {
    await jwtAxios
      .get(`api/chat/rooms`, {
        headers: {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
        },
      })
      .then(res => {
        console.log(res.data);
        setChatList(res.data.data);
      })
      .catch(err => {
        console.log(err);
      });
  };

  function GoGhatPage(id: number) {
    nav(`/party/chat/:${id}`);
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className='container'>
      <div className='navbar bg-base-100'>
        <div className='navbar-start'>
          <button className='btn btn-square btn-ghost'>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              fill='none'
              viewBox='0 0 24 24'
              className='inline-block w-8 h-8 stroke-current'>
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth='2'
                d='M15.41,16.58L10.83,12L15.41,7.41L14,6L8,12L14,18L15.41,16.58Z'></path>
            </svg>
          </button>
        </div>
        <div className='navbar-center'>
          <a className='text-xl'>채팅</a>
        </div>
        <div className='navbar-end'></div>
      </div>
      <div className='chat-list'>
        <div className='overflow-x-auto'>
          <table className='table'>
            <tbody>
              {/* row 1 */}
              <tr className=''>
                <td>
                  <div className='flex items-center gap-3'>
                    <div className='avatar w-12 h-12 relative'></div>

                    <div>
                      <div className='font-bold'>Hart Hagerty</div>
                    </div>
                  </div>
                </td>
                <td>Zemlak, Daniel and Leannon</td>
                <td>Purple</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ChatListPage;
