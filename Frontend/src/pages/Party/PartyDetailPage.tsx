import { useNavigate, useParams } from 'react-router-dom';
import MemberInfo from './components/MemberInfo';
import PartyInfo from './components/PartyInfo';
import PathMap from './components/PathMap';
import { useEffect, useState } from 'react';
import BottomButton from './components/BottomButton';
import jwtAxios from '@/utils/JWTUtil';
import { getCookie } from '@/utils/CookieUtil';
import TopNav from './components/TopNav';
import StateBadge from './components/StateBadge';
import { WebSocketProvider } from '@/context/webSocketProvider';

interface IInfo {
  id: number;
  createAt: string;
  modifiedAt: string;
  title: string;
  departuresName: string;
  departuresLocation: {
    latitude: number;
    longitude: number;
  };
  arrivalsName: string;
  arrivalsLocation: {
    latitude: number;
    longitude: number;
  };
  expectedCost: number;
  expectedDuration: number;
  departuresDate: string;
  currentParticipants: number;
  maxParticipants: number;
  category: string;
  genderRestriction: string;
  state: string;
  content: string;
  viewCount: number;
  requestCount: number;
  role: string;
  participants: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  }[];
  guests: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  }[];
  distance: number;
  path: number[][];
}

const PartyDetailPage = () => {
  const { partyId } = useParams();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState('');
  const [info, setInfo] = useState<IInfo>({
    id: 0,
    createAt: '',
    modifiedAt: '',
    title: 'OD!',
    departuresName: 'OD!',
    departuresLocation: {
      latitude: 0,
      longitude: 0,
    },
    arrivalsName: 'OD!',
    arrivalsLocation: {
      latitude: 0,
      longitude: 0,
    },
    expectedCost: 0,
    expectedDuration: 0,
    departuresDate: '',
    currentParticipants: 0,
    maxParticipants: 0,
    category: '',
    genderRestriction: '',
    state: '',
    content: '',
    viewCount: 0,
    requestCount: 0,
    role: '',
    participants: [
      {
        id: 0,
        role: '',
        nickname: '',
        gender: '',
        ageGroup: '',
        profileImage: '',
        isPaid: false,
      },
      {
        id: 0,
        role: '',
        nickname: '',
        gender: '',
        ageGroup: '',
        profileImage: '',
        isPaid: true,
      },
      // 추가 참가자들...
    ],

    guests: [
      {
        id: 0,
        role: '0',
        nickname: '',
        gender: '0',
        ageGroup: '',
        profileImage: '',
        isPaid: false,
      },
    ],
    distance: 0,
    path: [
      [0, 0],
      [0, 0],
    ],
  });

  const [hostInfo, setHostInfo] = useState({
    nickname: '',
    gender: '',
    ageGroup: '',
    profileImage: '',
  });
  const FindHost = (participants: any) => {
    return participants.find((p: { role: string }) => p.role === 'ORGANIZER');
  };

  const fetchData = async () => {
    await jwtAxios
      .get(`api/party-boards/${partyId}`, {
        headers: {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
        },
      })
      .then(res => {
        console.log(res.data);
        const pathInfoObject = JSON.parse(res.data.data.pathInfo);
        const data = pathInfoObject.route.traoptimal[0].summary;
        const path = pathInfoObject.route.traoptimal[0].path;

        setInfo(prevInfo => ({
          ...prevInfo,
          ...res.data.data,
          expectedCost: data.taxiFare,
          expectedDuration: Math.floor(data.duration / 60000),
          path: path,
          distance: data.distance / 1000,
        }));

        setHostInfo(FindHost(res.data.data.participants));
      })
      .catch(err => {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError('An unknown error occurred');
        }
      });

    setIsLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, []);

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = {
      month: 'long',
      day: '2-digit',
      weekday: 'short',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true,
    };
    return new Intl.DateTimeFormat('ko-KR', options).format(date);
  };

  function formatTimeDifference(createAt: string) {
    const now: Date = new Date();
    const createdTime: Date = new Date(createAt);
    const timeDifference: number = now.getTime() - createdTime.getTime(); // 현재 시간과 생성 시간의 차이 (밀리초 단위)
    const minutesDifference = Math.floor(timeDifference / (1000 * 60)); // 분 단위로 변환
    const hoursDifference = Math.floor(timeDifference / (1000 * 60 * 60)); // 시간 단위로 변환
    const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24)); // 일 단위로 변환

    if (daysDifference >= 1) {
      return `${daysDifference}일 전`;
    } else if (hoursDifference >= 1) {
      return `${hoursDifference}시간 전`;
    } else if (minutesDifference >= 1) {
      return `${minutesDifference}분 전`;
    } else {
      return '방금 전';
    }
  }

  if (isLoading)
    return (
      <div className='flex h-screen justify-center'>
        <span className='loading loading-ball loading-xs'></span>
        <span className='loading loading-ball loading-sm'></span>
        <span className='loading loading-ball loading-md'></span>
        <span className='loading loading-ball loading-lg'></span>
      </div>
    );
  if (error) return <p>{error}에런디유? 다시 시도해봐유 </p>;
  return (
    <div className='container'>
      <TopNav
        role={info.role}
        state={info.state}
        partyId={partyId}
        title={info.title}
        currentParticipants={info.currentParticipants}
        expectedCost={info.expectedCost}
        fetchData={fetchData}
      />

      <div className='party-info flex justify-between p-4 mt-14'>
        <p className='font-bold text-xl'>{formatDate(info.departuresDate)}</p>
        <StateBadge state={info.state} />
      </div>
      <div className='divider m-0'></div>
      {/* <p>{formatTimeDifference(info.createAt)}</p>
      <p>조회수: {info.viewCount}</p> */}
      <div className='path-map p-4'>
        <PathMap
          departuresName={info.departuresName}
          arrivalsName={info.arrivalsName}
          departuresX={info.departuresLocation.latitude}
          departuresY={info.departuresLocation.longitude}
          arrivalsX={info.arrivalsLocation.latitude}
          arrivalsY={info.arrivalsLocation.longitude}
          distance={info.distance}
          path={info.path}
        />
      </div>
      <div className='h-5 bg-slate-100'></div>
      <div className=''>
        <PartyInfo
          category={info.category}
          state={info.state}
          currentParticipants={info.currentParticipants}
          maxParticipants={info.maxParticipants}
          departuresDate={info.departuresDate}
          expectedCost={info.expectedCost}
          expectedTime={info.expectedDuration}
          content={info.content}
          genderRestriction={info.genderRestriction}
          hostName={hostInfo.nickname}
          hostImgUrl={hostInfo.profileImage}
        />
      </div>
      <div className='h-5 bg-slate-100'></div>
      <WebSocketProvider>
        <div className=''>
          <MemberInfo
            hostName={hostInfo.nickname}
            hostGender={hostInfo.gender}
            hostAge={hostInfo.ageGroup}
            hostImgUrl={hostInfo.profileImage}
            participants={info.participants}
            guests={info.guests}
            role={info.role}
            partyId={partyId}
            fetchData={fetchData}
          />
        </div>
      </WebSocketProvider>
      <div className='divider h-16'></div>
      <BottomButton
        state={info.state}
        role={info.role}
        partyId={partyId}
        fetchData={fetchData}
        hostGender={hostInfo.gender}
        genderRestriction={info.genderRestriction}
      />
    </div>
  );
};
export default PartyDetailPage;
