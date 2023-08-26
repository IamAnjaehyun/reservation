/* eslint-disable*/
import React, {useState} from 'react';
import logo from './logo.svg';
import './App.css';

function App() {

  let [글제목 ,글제목변경] = useState(['요즘 뜨는 식당', '요즘 뜨는 식당 2', '요즘 뜨는 식당 3']); //변수 대신 쓰는 데이터 저장공간
  let [좋아요, 좋아요변경] = useState(0);
  
  let styles = {color : 'gray', fontSize: '30px' }
  return (
    <div className="App">
      <div className='black-nav'>
      {/* 변경 자주 안되니까 걍둠 */}
        <div style={ styles }>Reservation</div> 
      </div>
      <div className='list'>
        <h3> {글제목[0]} <span onClick={ ()=>{좋아요변경(좋아요+1)} }>👍</span> {좋아요} </h3>
        <p>레스토랑</p>
        <hr/>
      </div>
      <div className='list'>
        <h3> {글제목[1]}</h3>
        <p>중식당</p>
        <hr/>
      </div>
      <div className='list'>
        <h3> {글제목[2]}</h3>
        <p>한식당</p>
        <hr/>
      </div>
    </div>
  );
}

export default App;
