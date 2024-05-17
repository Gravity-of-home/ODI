import React, { useState, useRef } from 'react';
import imageCompression from 'browser-image-compression';
import { useNavigate } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';
import { toast } from 'react-toastify';

interface ReportModalProps {
  partyId: string | undefined;
  reportedId: number;
  onClose: () => void;
}

const ReportModal: React.FC<ReportModalProps> = ({ partyId, onClose, reportedId }) => {
  const [imageFile, setImageFile] = useState<File | undefined>(undefined);
  const content = useRef<HTMLInputElement>(null);
  const [reportType, setReportType] = useState<string>('');

  const handleReportTypeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const value = event.target.value;
    setReportType(value);
  };

  async function handleImageUpload(event: File) {
    const options = {
      maxSizeMB: 1,
      maxWidthOrHeight: 1920,
      useWebWorker: true,
    };
    try {
      const compressedFile = await imageCompression(event, options);
      const file = new File([compressedFile], event.name, { type: event.type });

      return file;
    } catch (error) {
      console.log(error);
      return undefined;
    }
  }

  const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    if (
      event.target.files &&
      event.target.files[0] &&
      event.target.files[0].type.startsWith('image/')
    ) {
      try {
        const compressedImage = await handleImageUpload(event.target.files[0]);
        if (compressedImage) {
          setImageFile(compressedImage);
        } else {
          alert('이미지 압축에 실패했습니다.');
          setImageFile(undefined);
        }
      } catch (error) {
        console.error('Error compressing the image:', error);
        alert('Failed to compress image.');
        setImageFile(undefined);
      }
    } else {
      alert('이미지 파일만 업로드 가능합니다.');
      setImageFile(undefined);
    }
  };

  const reports = () => {
    const formData = new FormData();
    const messageContent = content.current?.value;

    if (!messageContent) {
      toast.error('신고사유는 필수 입니다');
      return;
    }
    if (!reportType) {
      toast.error('신고 유형을 선택해 주세요');
      return;
    }

    if (imageFile) {
      formData.append('attachments', imageFile);
    }

    formData.append('content', messageContent);
    // formData.append('roomId', roomId);
    formData.append('type', reportType);
    formData.append('partyId', partyId || '');
    formData.append('reportedId', reportedId.toString());

    jwtAxios
      .post(`/api/reports`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then(res => {
        if (res.data.status === 200) {
          toast.success(res.data.message, {
            pauseOnFocusLoss: false,
            hideProgressBar: true,
            closeOnClick: true,
          });
          onClose();
        }
      })
      .catch(err => {
        toast.error(err.response.data.reason);
        console.error(err);
      });
  };

  const handleModalClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation(); // 모달 내부 클릭 이벤트 처리
  };

  return (
    <div className='modal modal-open' onClick={handleModalClick}>
      <div className='modal-box relative'>
        <button className='btn btn-sm btn-circle absolute right-2 top-2' onClick={onClose}>
          ✕
        </button>
        <h3 className='font-bold text-lg mb-4'>신고하기</h3>
        <div className='form-control'>
          <select
            className='select select-info selected-sm w-2/3 '
            value={reportType}
            onChange={handleReportTypeChange}>
            <option value='' disabled>
              신고 유형
            </option>
            <option value='NON_PAYMENT'>미정산</option>
            <option value='FRAUD'>사기</option>
            <option value='DANGEROUS_BEHAVIOR'>위험한 행동</option>
            <option value='HARASSMENT'>성희롱/괴롭힘</option>
            <option value='INAPPROPRIATE_LANGUAGE_BEHAVIOR'>부적절한 언어/행동</option>
            <option value='NO_SHOW'>노쇼</option>
            <option value='OTHER'>기타</option>
          </select>
        </div>
        <div className='form-control'>
          <label className='label'>
            <span className='label-text'>신고사유</span>
          </label>
          <input
            type='text'
            ref={content}
            placeholder='신고 사유를 입력해 주세요'
            className='input input-bordered input-primary w-full max-w-xs'
          />
        </div>
        <div className='form-control mt-4'>
          <label className='label'>
            <span className='label-text'>이미지(선택)</span>
          </label>
          <input
            type='file'
            accept='image/*'
            onChange={handleFileChange}
            className='file-input file-input-bordered w-full max-w-xs'
          />
        </div>
        <div className='modal-action'>
          <button onClick={reports} className='btn btn-block btn-primary'>
            신고하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default ReportModal;
