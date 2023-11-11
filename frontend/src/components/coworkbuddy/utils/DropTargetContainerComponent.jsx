import React from 'react';
import { useDrop } from "react-dnd"
import DraggItemComponent from './DraggItemComponent';

const DropTargetContainerComponent = ({ data, onDrop }) => {

    const [{ isOver }, drop] = useDrop({
      accept: 'WORKER',
      drop: (worker) => onDrop(worker, data.taskId),
      collect: (monitor) => ({
        isOver: !!monitor.isOver(),
      }),
    });
  
    const workers = Array.isArray(data) ? data : data.workers;
  
    return (
      <div ref={drop} style={{ minHeight: '200px', border: '2px dashed #ccc', padding: '16px' }}>
        <div style={{ opacity: isOver ? 0.5 : 1 }}>
            {workers.map((worker, index) => (
              <DraggItemComponent key={`${worker.id}-${index}`} worker={worker} />
            ))}
        </div>
      </div>
    );
  };
  
  export default DropTargetContainerComponent;
