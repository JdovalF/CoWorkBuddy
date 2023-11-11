import React from 'react';
import { useDrop } from "react-dnd"
import DraggItemComponent from './DraggItemComponent';

const DropTargetContainerComponent = ({ data, onDrop, active }) => {

    const [{ isOver }, drop] = useDrop({
      accept: 'WORKER',
      drop: (worker) => onDrop(worker, data.taskId),
      collect: (monitor) => ({
        isOver: !!monitor.isOver(),
      }),
    });
  
    const workers = Array.isArray(data) ? data : data.workers;
    const cssStyle = {
       minHeight: '200px',
       minWidth: '300px',  
       padding: '0.5rem', 
       margin: '1rem', 
       borderRadius: '1rem', 
       display: 'flex'
      }
  
    return (
      <div ref={drop} style={!active ? {...cssStyle, border: '1px dashed #dc3545'} : {...cssStyle, border: '1px dashed #212529'}}>
        <div style={{ opacity: isOver ? 0.5 : 1 }}>
            {workers.map((worker, index) => (
              <DraggItemComponent key={`${worker.id}-${index}`} worker={worker} />
            ))}
        </div>
      </div>
    );
  };
  
  export default DropTargetContainerComponent;
