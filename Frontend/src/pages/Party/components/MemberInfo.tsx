import { useEffect, useState } from 'react';
import jwtAxios from '@/utils/JWTUtil';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface IMemberInfoProps {
  hostName: string;
  hostGender: string;
  hostAge: string;
  hostImgUrl: string;
  participants: any;
  guests: any;
  role: string;
  partyId: string | undefined;
}
interface IParticipant {
  id: number;
  role: string;
  nickname: string;
  gender: string;
  ageGroup: string;
  profileImage: string;
  isPaid: boolean;
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
  fetchData,
}) => {
  const [userId, setUserId] = useState<number | null>(null);

  useEffect(() => {
    const myData = localStorage.getItem('User');
    if (myData) {
      const userData = JSON.parse(myData);
      setUserId(userData.state.id); // 상태 업데이트
    }
  }, []);

  // 파티 신청 수락
  const acceptEnterParty = (memberId: number) => () => {
    jwtAxios
      .put(`api/parties/${partyId}/${memberId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 201) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
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
        }
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };

  // 파티원 추방
  const banParticipant = (memberId: number) => () => {
    jwtAxios
      .delete(`api/parties/${partyId}/${memberId}`, {})
      .then(res => {
        console.log(res.data);
        if (res.data.status === 204) {
          toast.success(`${res.data.message}`, { position: 'top-center' });
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
        </div>
        <div>
          {role === 'ORGANIZER' && ( // role이 'ORGANIZER'일 때만 버튼 렌더링
            <button
              onClick={banParticipant(person.id)}
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
      </div>
      {role === 'ORGANIZER' && (
        <div>
          <button
            onClick={acceptEnterParty(applicant.id)}
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
        <p className='mb-4 font-bold text-xl'>팟장</p>
        <div className='flex justify-between content-center'>
          <div className='flex gap-x-2 items-center'>
            <img className='rounded-full w-10 h-10' src={hostImgUrl} alt='팟장 이미지' />
            <p>{hostName}</p>
            <p>{hostGender === 'M' ? '남' : '여'}</p>
            <p>{hostAge}</p>
          </div>

          {role !== 'ORGANIZER' && (
            <p className=' rounded p-2' style={{ backgroundColor: '#A75DFC' }}>
              당도자리
            </p>
          )}
          {role === 'ORGANIZER' && (
            <p className='content-center text-center w-10 rounded-full bg-blue-100'>나</p>
          )}
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
