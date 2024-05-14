import { useEffect, useState } from 'react';
import jwtAxios from '@/utils/JWTUtil';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';
import { IParticipant } from '@/types/Party';
interface IMemberInfoProps {
  hostName: string;
  hostGender: string;
  hostAge: string;
  hostImgUrl: string;
  participants: any;
  guests: any;
  role: string;
  partyId: string | undefined;
  roomId: string;
}

const MemberInfo: React.FC<IMemberInfoProps & { fetchData: () => void }> = ({
  hostName,
  hostGender,
  hostAge,
  hostImgUrl,
  participants,
  guests,
  role,
  partyId,
  roomId,
  fetchData,
}) => {
  const [userId, setUserId] = useState<number | null>(null);
  const { client, isConnected } = useWebSocket();

  useEffect(() => {
    const myData = localStorage.getItem('User');
    if (myData) {
      const userData = JSON.parse(myData);
      setUserId(userData.state.id); // 상태 업데이트
    }
  }, []);

  const handleSendMessage = (type: string, id: number) => {
    if (client && client.connected) {
      client.publish({
        destination: `/pub/notification/${id}`,
        body: JSON.stringify({
          partyId: partyId,
          type: type,
        }),
        headers: {
          token: `${getCookie('Authorization')}`,
        },
      });
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  // 파티 신청 수락
  const acceptEnterParty = (memberId: number, nickname: string) => () => {
    jwtAxios
      .put(`api/parties/${partyId}/${memberId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 201) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
          client?.publish({
            destination: `/pub/chat/message`,
            body: JSON.stringify({
              partyId: partyId,
              roomId: roomId,
              content: `${nickname}님이 파티에 입장하셨습니다`,
              type: 'ENTER',
            }),
            headers: {
              token: `${getCookie('Authorization')}`,
            },
          });

          handleSendMessage('ACCEPT', memberId);
        }
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };

  // 파티 신청 거절
  const rejectEnterParty = (memberId: number) => () => {
    jwtAxios
      .delete(`api/parties/${partyId}/${memberId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 204) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
          client?.publish({
            destination: `/pub/notification/${memberId}`,
            body: JSON.stringify({
              partyId: partyId,
              // content: `${nickname}님이 파티에 입장하셨습니다`,
              type: 'REJECT',
            }),
            headers: {
              token: `${getCookie('Authorization')}`,
            },
          });
          handleSendMessage('REJECT', memberId);
        }
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };

  // 파티원 추방
  const banParticipant = (memberId: number, nickname: string) => () => {
    jwtAxios
      .delete(`api/parties/${partyId}/${memberId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 204) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
          client?.publish({
            destination: `/pub/chat/message`,
            body: JSON.stringify({
              partyId: partyId,
              roomId: roomId,
              content: `${nickname}님이 파티에서 추방되었습니다.`,
              type: 'QUIT',
            }),
            headers: {
              token: `${getCookie('Authorization')}`,
            },
          });
          handleSendMessage('KICK', memberId);
        }
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };

  // 파티원 목록
  const participantsList = participants
    .filter((person: IParticipant) => person.role !== 'ORGANIZER')
    .map((person: IParticipant) => (
      <li className='flex justify-between ' key={person.id}>
        <div className='flex gap-x-2 items-center'>
          <img className='rounded-full w-10 h-10' src={person.profileImage} alt='' />
          <p>{person.nickname}</p>
          <p>{person.gender === 'M' ? '남' : '여'}</p>
          <p>{person.ageGroup}</p>
          <p>{person.brix}</p>
        </div>
        <div>
          {role === 'ORGANIZER' && ( // role이 'ORGANIZER'일 때만 버튼 렌더링
            <button
              onClick={banParticipant(person.id, person.nickname)}
              className='ml-2 py-1 px-3 rounded bg-red-500 text-white'>
              추방
            </button>
          )}
        </div>
        {person.id === userId && (
          <div className='content-center text-center rounded-full w-10 bg-blue-100'>
            <p className='content-center text-center '>나</p>
          </div>
        )}
      </li>
    ));

  // 팟장에게 보일 파티 신청자 목록
  const applicantList = guests?.map((applicant: IParticipant) => (
    <li className='flex justify-between content-center' key={applicant.id}>
      <div className='user-profile flex gap-x-2 items-center'>
        <img src={applicant.profileImage} alt='user-img' className='rounded-full w-10 h-10' />
        <div>{applicant.nickname}</div>
        <div>{applicant.gender === 'M' ? '남' : '여'}</div>
        <div>{applicant.ageGroup}</div>
        <p className=' rounded p-2' style={{ backgroundColor: '#A75DFC' }}>
          {applicant.brix}
        </p>
      </div>
      {role === 'ORGANIZER' && (
        <div>
          <button
            onClick={acceptEnterParty(applicant.id, applicant.nickname)}
            className='ml-2 py-1 px-3 rounded bg-green-500 text-white'>
            수락
          </button>
          <button
            onClick={rejectEnterParty(applicant.id)}
            className='ml-2 py-1 px-3 rounded bg-red-500 text-white'>
            거절
          </button>
        </div>
      )}
    </li>
  ));

  return (
    <div className='container p-2'>
      <div className='host-info mb-5'>
        <p className='mb-4 font-bold text-xl'>
          팟장{' '}
          {role === 'ORGANIZER' && (
            <span className='content-center text-center w-10 rounded-full bg-blue-100'>나</span>
          )}
        </p>

        <div className='stats shadow'>
          <div className='stat'>
            <div className='stat-figure '>
              <div className='stat-value'>{hostName}</div>
              <div className='stat-title '>{hostAge}</div>
              <div className='stat-desc'>{hostGender}</div>
            </div>
            <div className='avatar'>
              <div className='w-16 rounded-full'>
                <img src={hostImgUrl} />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className='members'>
        <div className='mb-4 font-bold text-xl'>파티원</div>
        <div>
          {participantsList.length != 0 && <ul>{participantsList}</ul>}
          {participantsList.length == 0 && (
            <div className='h-56 flex justify-center '>
              <p className='content-center'>아직 파티원이 없어요</p>
            </div>
          )}
        </div>

        {/* 조회자가 팟장이고 파티신청자가 있으면 */}
        {applicantList && applicantList.length != 0 && (
          <div className='mt-4 mb-20'>
            <div>
              <p className='font-bold text-xl'>파티 신청자 목록</p>
            </div>
            <div className='p-1 mt-4 applicant-list'>
              <ul>{applicantList}</ul>
            </div>
          </div>
        )}
        {applicantList && applicantList.length == 0 && (
          <div className=' mt-4'>
            <div>
              <p className='font-bold text-xl'>매칭신청</p>
            </div>
            <div className='flex h-56 applicant-list justify-center items-center '>
              <p className='font-bold text-gray-400'>매칭 신청한 파티원이 없어요!</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
export default MemberInfo;
