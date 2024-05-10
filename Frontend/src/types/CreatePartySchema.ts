// import { z } from 'zod';

// export const Category = [
//   {
//     tag: 'DAILY',
//     name: '일상',
//   },
//   {
//     tag: 'UNIVERSITY',
//     name: '대학교',
//   },
//   {
//     tag: 'COMMUTE',
//     name: '출퇴근',
//   },
//   {
//     tag: 'CONCERT',
//     name: '콘서트',
//   },
//   {
//     tag: 'AIRPORT',
//     name: '공항',
//   },
//   {
//     tag: 'TRAVEL',
//     name: '여행',
//   },
//   {
//     tag: 'RESERVIST',
//     name: '예비군',
//   },
// ];

// const CategoryTag = Category.map(item => item.tag) as [string, ...string[]];

// const createPartySchema = z.object({
//   title: z
//     .string()
//     .min(3, { message: '제목은 3글자 이상이어야 합니다.' })
//     .max(50, { message: '제목은 50글자 이하여야 합니다.' }),
//   departuresName: z
//     .string()
//     .min(3, { message: '출발지를 입력해 주세요.' })
//     .max(50, { message: '출발지 주소는 50글자 이하여야 합니다.' }),
//   departuresLocation: z.object({
//     longitude: z.number(),
//     latitude: z.number(),
//   }),
//   arrivalsName: z
//     .string()
//     .min(3, { message: '도착지를 입력해 주세요.' })
//     .max(50, { message: '도착지 주소는 50글자 이하여야 합니다.' }),
//   arrivalsLocation: z.object({
//     longitude: z.number(),
//     latitude: z.number(),
//   }),
//   departuresDate: z.date(),
//   maxParticipants: z
//     .number()
//     .min(1, { message: '최소 1명 이상 탑승해야 해요!' })
//     .max(4, { message: '최대 4명만 탑승할 수 있어요!' })
//     .default(1),
//   category: z.enum(CategoryTag).refine(tag => tag !== '', { message: '카테고리를 선택해 주세요.' }),
//   genderRestriction: z.boolean(),
//   content: z.string().nullish(),
// });

// export default createPartySchema;
