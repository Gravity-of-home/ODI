import React, { useState, useRef } from 'react';
import userStore from '@/stores/useUserStore';
import axios from 'axios';
import imageCompression from 'browser-image-compression';
import { IUser } from '@/types/User';
import { ViteConfig } from '@/apis/ViteConfig';
import { getCookie } from '@/utils/CookieUtil';
import { toast } from 'react-toastify';

const ProfileDetail: React.FC = () => {
  const { id, name, nickname, image, email, gender, birth, ageGroup } = userStore();
  const [user, setUser] = useState<IUser>({
    id: id,
    name: name,
    nickname: nickname,
    image: image,
    email: email,
    gender: gender,
    birth: birth,
    ageGroup: ageGroup,
  });
  const [isEditing, setIsEditing] = useState(false);
  const [newNickname, setNickname] = useState<string | undefined>(user.nickname);
  const [imgFile, setImgFile] = useState<File | null>(null);
  const [previewImage, setPreviewImage] = useState<string | undefined>(user.image);

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handlePreview = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];

    if (file) {
      try {
        const options = {
          maxSizeMB: 1,
          maxWidthOrHeight: 500,
          useWebWorker: true,
        };
        const compressedFile = await imageCompression(file, options);
        const compressedImageUrl = URL.createObjectURL(compressedFile);
        setPreviewImage(compressedImageUrl);

        const newFile = new File([compressedFile], file.name, { type: file.type });
        setImgFile(newFile);
      } catch (error) {
        console.error('미리보기 이미지 압축 실패 : ', error);
      }
    }
  };

  const handleEditData = async () => {
    try {
      const formData = new FormData();
      if (newNickname !== user.nickname) {
        formData.append('newNickname', newNickname as string);
      }
      if (previewImage) {
        formData.append('newImage', imgFile as File);
      }

      const response = await axios.put(`${ViteConfig.VITE_BASE_URL}/api/members/me`, formData, {
        headers: {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.status === 200) {
        toast.success('회원정보 수정이 완료되었습니다.');
        setUser({
          ...user,
          nickname: newNickname,
          image: previewImage || user.image,
        });
        setIsEditing(false);
      }
    } catch (error) {
      console.error('회원정보 수정 실패 : ', error);
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
    setNickname(user.nickname);
    setPreviewImage(user.image as string);
    setImgFile(null);
  };

  return (
    <div className=''>
      {isEditing ? (
        <>
          <div>
            <label>
              닉네임:
              <input type='text' value={newNickname} onChange={e => setNickname(e.target.value)} />
            </label>
          </div>
          <div>
            <label>
              이미지 업로드:
              <input type='file' accept='image/*' onChange={handlePreview} />
            </label>
          </div>
          {previewImage ? (
            <div>
              <p>이미지 미리보기:</p>
              <img src={previewImage} alt='미리보기' />
            </div>
          ) : (
            <div>
              <p>현재 이미지:</p>
              <img src={user.image} alt={`${user.nickname}의 프로필`} />
            </div>
          )}
          <button onClick={handleEditData}>저장</button>
          <button onClick={handleCancel}>취소</button>
        </>
      ) : (
        <>
          <h2>사용자 정보</h2>
          <p>이메일: {user.email}</p>
          <p>나이대: {user.ageGroup}</p>
          <p>이름: {user.name}</p>
          <p>닉네임: {user.nickname}</p>
          <p>성별: {user.gender}</p>
          <img src={user.image} alt={`${user.nickname}의 프로필`} />
          <button onClick={handleEditClick}>수정하기</button>
        </>
      )}
    </div>
  );
};

export default ProfileDetail;
