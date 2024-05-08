// import createPartySchema from '@/types/CreatePartySchema';
// import { Category } from '@/types/CreatePartySchema';
// import { useForm } from 'react-hook-form';
// import { zodResolver } from '@hookform/resolvers/zod';

// const PartyCreatePage = () => {
//   const {
//     register,
//     handleSubmit,
//     formState: { errors },
//   } = useForm({
//     resolver: zodResolver(createPartySchema),
//   });

//   // const onSubmit = async data => {
//   //   try {
//   //     const response = await axios.post('https://example.com/api/parties', data);
//   //     alert('예약이 성공적으로 생성되었습니다!');
//   //     console.log(response.data);
//   //   } catch (error) {
//   //     console.error('예약 생성 실패:', error);
//   //     alert('예약 생성에 실패했습니다.');
//   //   }
//   // };

//   return (
//     <form
//       // onSubmit={handleSubmit(onSubmit)}
//       className='space-y-4 p-5 max-w-lg mx-auto bg-white shadow-lg rounded-lg'>
//       <div>
//         <label className='block text-sm font-medium text-gray-700'>제목</label>
//         <input
//           type='text'
//           {...register('title')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//         />
//         <p className='mt-1 text-sm text-red-500'>
//           {errors.title && typeof errors.title.message === 'string' ? errors.title.message : null}
//         </p>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>출발지 이름</label>``
//         <input
//           type='text'
//           {...register('departuresName')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//         />
//         <p className='mt-1 text-sm text-red-500'>
//           {errors.departuresName && typeof errors.departuresName.message === 'string'
//             ? errors.departuresName.message
//             : null}
//         </p>
//       </div>

//       <div className='grid grid-cols-2 gap-4'>
//         <div>
//           <label className='block text-sm font-medium text-gray-700'>출발지 경도</label>
//           <input
//             type='number'
//             step='0.000001'
//             {...register('departuresLocation.longitude')}
//             className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//           />
//         </div>
//         <div>
//           <label className='block text-sm font-medium text-gray-700'>출발지 위도</label>
//           <input
//             type='number'
//             step='0.000001'
//             {...register('departuresLocation.latitude')}
//             className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//           />
//         </div>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>도착지 이름</label>
//         <input
//           type='text'
//           {...register('arrivalsName')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//         />

//         <p className='mt-1 text-sm text-red-500'>
//           {errors.arrivalsName && typeof errors.arrivalsName.message === 'string'
//             ? errors.arrivalsName.message
//             : null}
//         </p>
//       </div>

//       <div className='grid grid-cols-2 gap-4'>
//         <div>
//           <label className='block text-sm font-medium text-gray-700'>도착지 경도</label>
//           <input
//             type='number'
//             step='0.000001'
//             {...register('arrivalsLocation.longitude')}
//             className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//           />
//         </div>
//         <div>
//           <label className='block text-sm font-medium text-gray-700'>도착지 위도</label>
//           <input
//             type='number'
//             step='0.000001'
//             {...register('arrivalsLocation.latitude')}
//             className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//           />
//         </div>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>출발 일시</label>
//         <input
//           type='datetime-local'
//           {...register('departuresDate')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//         />

//         <p className='mt-1 text-sm text-red-500'>
//           {errors.departuresDate && typeof errors.departuresDate.message === 'string'
//             ? errors.departuresDate.message
//             : null}
//         </p>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>최대 참가자 수</label>
//         <input
//           type='number'
//           {...register('maxParticipants')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//         />

//         <p className='mt-1 text-sm text-red-500'>
//           {errors.maxParticipants && typeof errors.maxParticipants.message === 'string'
//             ? errors.maxParticipants.message
//             : null}
//         </p>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>카테고리</label>
//         <select
//           {...register('category')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'>
//           <option value=''>카테고리를 선택하세요</option>
//           {Category.map(({ tag, name }) => (
//             <option key={tag} value={tag}>
//               {name}
//             </option>
//           ))}
//         </select>
//         {/* <p className='mt-1 text-sm text-red-500'>
//           {errors.category && typeof errors.category.message === 'string'
//             ? errors.category.message
//             : null}
//         </p> */}
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>성별 제한 (true/false)</label>
//         <input
//           type='checkbox'
//           {...register('genderRestriction')}
//           className='mt-1 block px-3 py-2'
//         />

//         <p className='mt-1 text-sm text-red-500'>
//           {errors.genderRestriction && typeof errors.genderRestriction.message === 'string'
//             ? errors.genderRestriction.message
//             : null}
//         </p>
//       </div>

//       <div>
//         <label className='block text-sm font-medium text-gray-700'>추가 내용</label>
//         <textarea
//           {...register('content')}
//           className='mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500'
//           rows={3}></textarea>
//       </div>

//       <button
//         type='submit'
//         className='inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-500 hover:bg-blue-700'>
//         예약 생성
//       </button>
//     </form>
//   );
// };

// export default PartyCreatePage;
