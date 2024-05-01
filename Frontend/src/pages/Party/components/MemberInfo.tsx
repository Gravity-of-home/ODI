import { useEffect } from 'react';
import axios from 'axios';
//TODO : 상황에 따라 다른 화면을 보여줘야 합니다.

interface IMemberInfoProps {
  hostName: string;
  hostGender: string;
  hostAge: string;
  hostImgUrl: string;
  participants: any;
  guests: any;
  role: string;
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
  fetchData,
}) => {
  const acceptEnterParty = (memberId: number) => () => {
    axios
      .put(`http://localhost:8080/api/parties/1/${memberId}`, {
        headers: {
          AUTHORIZATION:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpZCI6IjEiLCJpYXQiOjE3MTQzNTIwNzEsImV4cCI6MTcxNDM1NTA3MX0.BrfMWcpzfk3nSPApcjzZm7Wce6z_3DtELt2BNlddxQE',
        },
      })
      .then(response => {
        console.log(response.data);
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };
  const rejectEnterParty = (memberId: number) => () => {
    axios
      .delete(`http://localhost:8080/api/parties/1/${memberId}`, {
        headers: {
          AUTHORIZATION:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpZCI6IjEiLCJpYXQiOjE3MTQzNTIwNzEsImV4cCI6MTcxNDM1NTA3MX0.BrfMWcpzfk3nSPApcjzZm7Wce6z_3DtELt2BNlddxQE',
        },
      })
      .then(response => {
        console.log(response.data);
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };
  // TODO 참여 요청 거절이랑 URI, Method 같음 수정 해야함
  const banParticipant = (memberId: number) => () => {
    axios
      .delete(`http://localhost:8080/api/parties/1/${memberId}`, {
        headers: {
          AUTHORIZATION:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpZCI6IjEiLCJpYXQiOjE3MTQzNTIwNzEsImV4cCI6MTcxNDM1NTA3MX0.BrfMWcpzfk3nSPApcjzZm7Wce6z_3DtELt2BNlddxQE',
        },
      })
      .then(response => {
        console.log(response.data);
        fetchData();
      })
      .catch(err => {
        console.error(err);
      });
  };

  const participantsList = participants.map((person: IParticipant) => <li>{person.nickname}</li>);

  const participantsListForOrganizer = participants
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
      </li>
    ));

  const applicantList = guests?.map((applicant: IParticipant) => (
    <li className='flex justify-between content-center' key={applicant.id}>
      <div className='user-profile flex gap-x-2 items-center'>
        <img src={applicant.profileImage} alt='user-img' className='rounded-full w-10 h-10' />
        <div>{applicant.nickname}</div>
        <div>{applicant.gender === 'M' ? '남' : '여'}</div>
        <div>{applicant.ageGroup}</div>
      </div>
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

          {role !== 'ORGANIZER' && <button className='bg-slate-300 rounded p-2'>1:1채팅</button>}
          {role === 'ORGANIZER' && (
            <p className='content-center text-center w-10 rounded-full bg-blue-100'>나</p>
          )}
        </div>
      </div>
      <div className='members'>
        <div className='mb-4 font-bold text-xl'>파티원</div>
        <div>
          {participantsListForOrganizer.length != 0 && <ul>{participantsListForOrganizer}</ul>}
          {participantsListForOrganizer.length == 0 && (
            <div className='h-56 flex justify-center '>
              <p className='content-center'>아직 파티원이 없어요</p>
            </div>
          )}
        </div>

        {/* 조회자가 팟장이고 파티신청자가 있으면 */}
        {role == 'ORGANIZER' && applicantList.length != 0 && (
          <div className='mt-4'>
            <div>
              <p className='font-bold text-xl'>파티 신청자 목록</p>
            </div>
            <div className='p-1 mt-4 applicant-list '>
              <ul>{applicantList}</ul>
            </div>
          </div>
        )}
        {role == 'ORGANIZER' && applicantList.length == 0 && (
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
