/* eslint-disable */
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import logo from './logo.svg';
import './App.css';

function App() {
  let [글목록, 글목록변경] = useState([]);
  let [좋아요, 좋아요변경] = useState(0);
  const [상점선택, 상점변경] = useState(null); // State to store the selected name

  useEffect(() => {
    // 네트워크 요청을 보내어 JSON 데이터 받아오기
    axios.get('http://15.164.105.168:8080/v1/guest/store')
      .then(response => {
        // 받아온 데이터의 content 배열의 name 값만 추출하여 글제목에 설정
        글목록변경(response.data.body.storeList.content);
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []); // 빈 배열을 두 번째 인자로 전달하여 최초 렌더링 시에만 호출되도록 설정
  let styles = { color: 'gray', fontSize: '30px' };
  const 선택한상점변경 = (name) => {
    상점변경(name);
  };

  const 모달닫기 = () => {
    상점변경(null); // Clear the selected 글 when closing the modal
  };

  const fetchStoreDetails = (storeId) => {
    axios.get(`http://15.164.105.168:8080/v1/guest/store/${storeId}`)
      .then(response => {
        상점변경(response.data.body.store);
      })
      .catch(error => {
        console.error('Error fetching store data:', error);
      });
  };

  return (
    <div className="App">
      <div className='black-nav'>
        {/* 변경 자주 안되니까 걍둠 */}
        <div style={styles}>Reservation</div>
      </div>
    
      {글목록.map((글, index) => (
        <div className='list' key={index}>
          <h3 onClick={() => fetchStoreDetails(글.storeId)}> {글.name} <span onClick={() => { 
            글목록변경(prevState => {
              const 새목록 = [...prevState];
              새목록[index] = { ...새목록[index], favoriteCount: 새목록[index].favoriteCount + 1 };
              return 새목록;
            });
          }}>👍</span> {글.favoriteCount}</h3>
          <p>전화번호: {글.phoneNum}</p>
          <p>거리: {글.description}</p>
          <p>평균 평점: {글.averageRating}</p>
          <p>리뷰 수: {글.totalReviewCount}</p>
          <hr />
        </div>
      ))}
      <Modal 선택한상점={상점선택} 모달닫기={모달닫기} />
    </div>
  );
}

function Modal({ 선택한상점, 모달닫기 }) {
  const [댓글리스트, set댓글리스트] = useState([]);
  const [댓글보기, 댓글보기변경] = useState(false);

  useEffect(() => {
    if (선택한상점) {
      axios.get(`http://15.164.105.168:8080/v1/reservation/user/review/stores/${선택한상점.storeId}`)
        .then(response => {
          set댓글리스트(response.data.body);
        })
        .catch(error => {
          console.error('Error fetching comments:', error);
        });
    }
  }, [선택한상점]);

  const 토글댓글보기 = () => {
    댓글보기변경(!댓글보기);
  };

  return (
    <div className='modal'>
      {선택한상점 ? (
        <>
          <h2>선택한 상점: {선택한상점.name}</h2>
          <p>전화번호: {선택한상점.phoneNum}</p>
          <p>위치: {선택한상점.location}</p>
          <p>거리: {선택한상점.description}</p>
          <p>평균 평점: {선택한상점.averageRating}</p>
          <p>리뷰 수: {선택한상점.totalReviewCount}</p>
          <button onClick={토글댓글보기}>
            {댓글보기 ? '댓글 숨기기' : '댓글 보기'}
          </button>
          {댓글보기 && (
            <>
              <h3>댓글 리스트</h3>
              <ul>
                {댓글리스트.map((댓글) => (
                  <li key={댓글.reviewId}>
                    <p>사용자 이름: {댓글.userName}</p>
                    <p>평점: {댓글.stars}</p>
                    <p>리뷰 내용: {댓글.reviewText}</p>
                  </li>
                ))}
              </ul>
            </>
          )}
        </>
      ) : (
        <p>상세보기를 원하는 상점의 제목을 클릭해주세요.</p>
      )}
      <button onClick={모달닫기}>닫기</button>
    </div>
  );
}


export default App;